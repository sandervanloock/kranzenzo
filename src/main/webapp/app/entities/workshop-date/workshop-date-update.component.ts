import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';
import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from 'app/entities/workshop';

@Component({
    selector: 'jhi-workshop-date-update',
    templateUrl: './workshop-date-update.component.html'
})
export class WorkshopDateUpdateComponent implements OnInit {
    isSaving: boolean;
    workshops: IWorkshop[];
    date: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private workshopDateService: WorkshopDateService,
        private workshopService: WorkshopService,
        private activatedRoute: ActivatedRoute
    ) {}

    private _workshopDate: IWorkshopDate;

    get workshopDate() {
        return this._workshopDate;
    }

    set workshopDate(workshopDate: IWorkshopDate) {
        this._workshopDate = workshopDate;
        this.date = moment(workshopDate.date).format(DATE_TIME_FORMAT);
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ workshopDate }) => {
            this.workshopDate = workshopDate;
        });
        this.workshopService.query().subscribe(
            (res: HttpResponse<IWorkshop[]>) => {
                this.workshops = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.workshopDate.date = moment(this.date, DATE_TIME_FORMAT);
        if (this.workshopDate.id !== undefined) {
            this.subscribeToSaveResponse(this.workshopDateService.update(this.workshopDate));
        } else {
            this.subscribeToSaveResponse(this.workshopDateService.create(this.workshopDate));
        }
    }

    trackWorkshopById(index: number, item: IWorkshop) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshopDate>>) {
        result.subscribe((res: HttpResponse<IWorkshopDate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
