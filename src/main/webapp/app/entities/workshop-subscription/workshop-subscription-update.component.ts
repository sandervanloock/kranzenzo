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
import { IWorkshopSubscription, WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from './workshop-subscription.service';
import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from 'app/entities/workshop-date/workshop-date.service';

@Component({
  selector: 'jhi-workshop-subscription-update',
  templateUrl: './workshop-subscription-update.component.html'
})
export class WorkshopSubscriptionUpdateComponent implements OnInit {
  isSaving: boolean;

  workshopdates: IWorkshopDate[];

  editForm = this.fb.group({
    id: [],
    created: [],
    state: [],
    workshopId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected workshopSubscriptionService: WorkshopSubscriptionService,
    protected workshopDateService: WorkshopDateService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ workshopSubscription }) => {
      this.updateForm(workshopSubscription);
    });
    this.workshopDateService
      .query()
      .subscribe(
        (res: HttpResponse<IWorkshopDate[]>) => (this.workshopdates = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(workshopSubscription: IWorkshopSubscription) {
    this.editForm.patchValue({
      id: workshopSubscription.id,
      created: workshopSubscription.created != null ? workshopSubscription.created.format(DATE_TIME_FORMAT) : null,
      state: workshopSubscription.state,
      workshopId: workshopSubscription.workshopId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const workshopSubscription = this.createFromForm();
    if (workshopSubscription.id !== undefined) {
      this.subscribeToSaveResponse(this.workshopSubscriptionService.update(workshopSubscription));
    } else {
      this.subscribeToSaveResponse(this.workshopSubscriptionService.create(workshopSubscription));
    }
  }

  private createFromForm(): IWorkshopSubscription {
    return {
      ...new WorkshopSubscription(),
      id: this.editForm.get(['id']).value,
      created: this.editForm.get(['created']).value != null ? moment(this.editForm.get(['created']).value, DATE_TIME_FORMAT) : undefined,
      state: this.editForm.get(['state']).value,
      workshopId: this.editForm.get(['workshopId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshopSubscription>>) {
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

  trackWorkshopDateById(index: number, item: IWorkshopDate) {
    return item.id;
  }
}
