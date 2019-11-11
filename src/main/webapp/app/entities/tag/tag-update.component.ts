import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from 'app/entities/workshop/workshop.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { IImage } from 'app/shared/model/image.model';
import { ImageService } from 'app/entities/image/image.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html'
})
export class TagUpdateComponent implements OnInit {
  isSaving: boolean;

  workshops: IWorkshop[];

  products: IProduct[];

  images: IImage[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    homepage: [],
    imageId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tagService: TagService,
    protected workshopService: WorkshopService,
    protected productService: ProductService,
    protected ImageService: ImageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);
    });
    this.workshopService
      .query()
      .subscribe((res: HttpResponse<IWorkshop[]>) => (this.workshops = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.productService
      .query()
      .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.ImageService.query({ filter: 'tag-is-null' }).subscribe(
      (res: HttpResponse<IImage[]>) => {
        if (!this.editForm.get('imageId').value) {
          this.images = res.body;
        } else {
          this.ImageService.find(this.editForm.get('imageId').value).subscribe(
            (subRes: HttpResponse<IImage>) => (this.images = [subRes.body].concat(res.body)),
            (subRes: HttpErrorResponse) => this.onError(subRes.message)
          );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(tag: ITag) {
    this.editForm.patchValue({
      id: tag.id,
      name: tag.name,
      homepage: tag.homepage,
      imageId: tag.imageId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  private createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      homepage: this.editForm.get(['homepage']).value,
      imageId: this.editForm.get(['imageId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackWorkshopById(index: number, item: IWorkshop) {
    return item.id;
  }

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }

  trackImageById(index: number, item: IImage) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
