import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { Principal } from 'app/core';
import { WorkshopDateService } from './workshop-date.service';

@Component({
    selector: 'jhi-workshop-date',
    templateUrl: './workshop-date.component.html'
})
export class WorkshopDateComponent implements OnInit, OnDestroy {
    workshopDates: IWorkshopDate[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private workshopDateService: WorkshopDateService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.workshopDateService.query().subscribe(
            (res: HttpResponse<IWorkshopDate[]>) => {
                this.workshopDates = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWorkshopDates();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWorkshopDate) {
        return item.id;
    }

    registerChangeInWorkshopDates() {
        this.eventSubscriber = this.eventManager.subscribe('workshopDateListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
