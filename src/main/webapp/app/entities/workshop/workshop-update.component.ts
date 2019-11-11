import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IWorkshop, Workshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag/tag.service';

@Component({
  selector: 'jhi-workshop-update',
  templateUrl: './workshop-update.component.html'
})
export class WorkshopUpdateComponent implements OnInit {
  isSaving: boolean;

  tags: ITag[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(255)]],
    description: [null, [Validators.maxLength(5000)]],
    neededMaterials: [null, [Validators.maxLength(5000)]],
    includedInPrice: [null, [Validators.maxLength(5000)]],
    durationInMinutes: [null, [Validators.required]],
    price: [null, [Validators.min(0)]],
    maxSubscriptions: [],
    isActive: [],
    showOnHomepage: [],
    tags: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected workshopService: WorkshopService,
    protected tagService: TagService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ workshop }) => {
      this.updateForm(workshop);
    });
    this.tagService
      .query()
      .subscribe((res: HttpResponse<ITag[]>) => (this.tags = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(workshop: IWorkshop) {
    this.editForm.patchValue({
      id: workshop.id,
      name: workshop.name,
      description: workshop.description,
      neededMaterials: workshop.neededMaterials,
      includedInPrice: workshop.includedInPrice,
      durationInMinutes: workshop.durationInMinutes,
      price: workshop.price,
      maxSubscriptions: workshop.maxSubscriptions,
      isActive: workshop.isActive,
      showOnHomepage: workshop.showOnHomepage,
      tags: workshop.tags
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const workshop = this.createFromForm();
    if (workshop.id !== undefined) {
      this.subscribeToSaveResponse(this.workshopService.update(workshop));
    } else {
      this.subscribeToSaveResponse(this.workshopService.create(workshop));
    }
  }

  private createFromForm(): IWorkshop {
    return {
      ...new Workshop(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      neededMaterials: this.editForm.get(['neededMaterials']).value,
      includedInPrice: this.editForm.get(['includedInPrice']).value,
      durationInMinutes: this.editForm.get(['durationInMinutes']).value,
      price: this.editForm.get(['price']).value,
      maxSubscriptions: this.editForm.get(['maxSubscriptions']).value,
      isActive: this.editForm.get(['isActive']).value,
      showOnHomepage: this.editForm.get(['showOnHomepage']).value,
      tags: this.editForm.get(['tags']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshop>>) {
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

  trackTagById(index: number, item: ITag) {
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
