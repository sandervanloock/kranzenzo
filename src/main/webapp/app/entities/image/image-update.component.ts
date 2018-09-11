import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IImage } from 'app/shared/model/image.model';
import { ImageService } from './image.service';
import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from 'app/entities/workshop';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-image-update',
    templateUrl: './image-update.component.html'
})
export class ImageUpdateComponent implements OnInit {
    isSaving: boolean;
    workshops: IWorkshop[];
    products: IProduct[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private imageService: ImageService,
        private workshopService: WorkshopService,
        private productService: ProductService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    private _image: IImage;

    get image() {
        return this._image;
    }

    set image(image: IImage) {
        this._image = image;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ image }) => {
            this.image = image;
        });
        this.workshopService.query().subscribe(
            (res: HttpResponse<IWorkshop[]>) => {
                this.workshops = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productService.query().subscribe(
            (res: HttpResponse<IProduct[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.image, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.image.id !== undefined) {
            this.subscribeToSaveResponse(this.imageService.update(this.image));
        } else {
            this.subscribeToSaveResponse(this.imageService.create(this.image));
        }
    }

    trackWorkshopById(index: number, item: IWorkshop) {
        return item.id;
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>) {
        result.subscribe((res: HttpResponse<IImage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
