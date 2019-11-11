import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IWorkshopDate, WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';
import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from 'app/entities/workshop/workshop.service';

@Component({
  selector: 'jhi-workshop-date-update',
  templateUrl: './workshop-date-update.component.html'
})
export class WorkshopDateUpdateComponent implements OnInit {
  isSaving: boolean;

  workshops: IWorkshop[];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    workshopId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected workshopDateService: WorkshopDateService,
    protected workshopService: WorkshopService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ workshopDate }) => {
      this.updateForm(workshopDate);
    });
    this.workshopService
      .query()
      .subscribe((res: HttpResponse<IWorkshop[]>) => (this.workshops = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(workshopDate: IWorkshopDate) {
    this.editForm.patchValue({
      id: workshopDate.id,
      date: workshopDate.date != null ? workshopDate.date.format(DATE_TIME_FORMAT) : null,
      workshopId: workshopDate.workshopId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const workshopDate = this.createFromForm();
    if (workshopDate.id !== undefined) {
      this.subscribeToSaveResponse(this.workshopDateService.update(workshopDate));
    } else {
      this.subscribeToSaveResponse(this.workshopDateService.create(workshopDate));
    }
  }

  private createFromForm(): IWorkshopDate {
    return {
      ...new WorkshopDate(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      workshopId: this.editForm.get(['workshopId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshopDate>>) {
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
}
