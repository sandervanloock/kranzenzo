import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { IImage, Image } from 'app/shared/model/image.model';
import { FileUploader } from 'ng2-file-upload/ng2-file-upload';

@Component({
    selector: 'jhi-product-update',
    templateUrl: './product-update.component.html',
    styleUrls: ['./product-update.component.css']
})
export class ProductUpdateComponent implements OnInit {
    isSaving: boolean;
    tags: ITag[];
    imageEndpoints: string[] = [];
    uploader: FileUploader;

    private _product: IProduct;

    constructor(
        private jhiAlertService: JhiAlertService,
        private productService: ProductService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute,
        private authServer: AuthServerProvider,
        private s3ImageResizePipe: S3ImageResizePipe
    ) {}

    get product() {
        return this._product;
    }

    set product(product: IProduct) {
        this._product = product;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        const headers = [
            {
                name: 'Authorization',
                value: `Bearer ${this.authServer.getToken()}`
            }
        ];
        this.uploader = new FileUploader({
            url: '/api/images/raw',
            itemAlias: 'file',
            authTokenHeader: this.authServer.getToken(),
            headers // allowedFileType: ['.png', '.jpeg', '.jpg']
        });
        this.uploader.onCompleteItem = (item: any, response: any, status: any) => {
            this.onImageUploadFinished(response);
        };
        if (this.product) {
            if (!this.product.images) {
                this.product.images = [];
            }
            this.product.images.forEach((image: IImage) => {
                this.imageEndpoints.push(this.s3ImageResizePipe.transform(image.endpoint, '250x250'));
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.product.id !== undefined) {
            this.subscribeToSaveResponse(this.productService.update(this.product));
        } else {
            this.subscribeToSaveResponse(this.productService.create(this.product));
        }
    }

    onFileSelected() {
        this.uploader.uploadAll();
    }

    onImageRemoved(imageEndpoint: String) {
        this.product.images.forEach((image: IImage, index: number) => {
            if (this.s3ImageResizePipe.transform(image.endpoint, '250x250') === imageEndpoint) {
                this.product.images.splice(index, 1);
                this.imageEndpoints.splice(index, 1);
            }
        });
    }

    onImageUploadFinished($event: any) {
        const parsedJson = JSON.parse($event);
        const id = parsedJson.id;
        if (id) {
            const image = new Image(id);
            image.endpoint = parsedJson.endpoint;
            this.product.images.push(image);
            this.imageEndpoints.push(this.s3ImageResizePipe.transform(image.endpoint, '250x250'));
        }
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>) {
        result.subscribe((res: HttpResponse<IProduct>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
