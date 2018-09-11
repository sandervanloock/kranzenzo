import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IWorkshop } from 'app/shared/model/workshop.model';
import { Principal } from 'app/core';
import { WorkshopService } from './workshop.service';

@Component({
    selector: 'jhi-workshop',
    templateUrl: './workshop.component.html'
})
export class WorkshopComponent implements OnInit, OnDestroy {
    workshops: IWorkshop[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private workshopService: WorkshopService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.workshopService.query().subscribe(
            (res: HttpResponse<IWorkshop[]>) => {
                this.workshops = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWorkshops();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWorkshop) {
        return item.id;
    }

    registerChangeInWorkshops() {
        this.eventSubscriber = this.eventManager.subscribe('workshopListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
