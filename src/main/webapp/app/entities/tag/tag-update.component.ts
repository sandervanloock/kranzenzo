import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from 'app/entities/workshop';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { ImageUploadComponent } from 'app/shared/image/image-upload/image-upload.component';

@Component({
    selector: 'jhi-tag-update',
    templateUrl: './tag-update.component.html'
})
export class TagUpdateComponent implements OnInit {
    isSaving: boolean;
    workshops: IWorkshop[];
    products: IProduct[];
    tags: ITag[];
    @ViewChild(ImageUploadComponent) private imageUpload: ImageUploadComponent;
    private _tag: ITag;

    constructor(
        private jhiAlertService: JhiAlertService,
        private tagService: TagService,
        private workshopService: WorkshopService,
        private productService: ProductService,
        private activatedRoute: ActivatedRoute
    ) {}

    get tag() {
        return this._tag;
    }

    set tag(tag: ITag) {
        this._tag = tag;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tag }) => {
            this.tag = tag;
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
        this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => {
            this.tags = res.body;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        if (this.imageUpload) {
            this.tag.image = this.imageUpload.images.length ? this.imageUpload.images[0] : null;
        }
        this.isSaving = true;
        if (this.tag.id !== undefined) {
            this.subscribeToSaveResponse(this.tagService.update(this.tag));
        } else {
            this.subscribeToSaveResponse(this.tagService.create(this.tag));
        }
    }

    trackWorkshopById(index: number, item: IWorkshop) {
        return item.id;
    }

    trackProductById(index: number, item: IProduct) {
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>) {
        result.subscribe((res: HttpResponse<ITag>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
