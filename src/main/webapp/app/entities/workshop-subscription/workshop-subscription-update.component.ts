import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from './workshop-subscription.service';
import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from 'app/entities/workshop-date';

@Component({
    selector: 'jhi-workshop-subscription-update',
    templateUrl: './workshop-subscription-update.component.html'
})
export class WorkshopSubscriptionUpdateComponent implements OnInit {
    isSaving: boolean;
    workshopdates: IWorkshopDate[];
    created: string;
    private _workshopSubscription: IWorkshopSubscription;

    constructor(
        private jhiAlertService: JhiAlertService,
        private workshopSubscriptionService: WorkshopSubscriptionService,
        private workshopDateService: WorkshopDateService,
        private activatedRoute: ActivatedRoute
    ) {}

    get workshopSubscription() {
        return this._workshopSubscription;
    }

    set workshopSubscription(workshopSubscription: IWorkshopSubscription) {
        this._workshopSubscription = workshopSubscription;
        this.created = moment(workshopSubscription.created).format(DATE_TIME_FORMAT);
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ workshopSubscription }) => {
            this.workshopSubscription = workshopSubscription;
        });
        this.workshopDateService.query().subscribe(
            (res: HttpResponse<IWorkshopDate[]>) => {
                this.workshopdates = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.workshopSubscription.created = moment(this.created, DATE_TIME_FORMAT);
        if (this.workshopSubscription.id !== undefined) {
            this.subscribeToSaveResponse(this.workshopSubscriptionService.update(this.workshopSubscription));
        } else {
            this.subscribeToSaveResponse(this.workshopSubscriptionService.create(this.workshopSubscription));
        }
    }

    trackWorkshopDateById(index: number, item: IWorkshopDate) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshopSubscription>>) {
        result.subscribe(
            (res: HttpResponse<IWorkshopSubscription>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
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
