import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { Principal } from 'app/core';
import { WorkshopSubscriptionService } from './workshop-subscription.service';

@Component({
    selector: 'jhi-workshop-subscription',
    templateUrl: './workshop-subscription.component.html'
})
export class WorkshopSubscriptionComponent implements OnInit, OnDestroy {
    workshopSubscriptions: IWorkshopSubscription[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private workshopSubscriptionService: WorkshopSubscriptionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.workshopSubscriptionService.query().subscribe(
            (res: HttpResponse<IWorkshopSubscription[]>) => {
                this.workshopSubscriptions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWorkshopSubscriptions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWorkshopSubscription) {
        return item.id;
    }

    registerChangeInWorkshopSubscriptions() {
        this.eventSubscriber = this.eventManager.subscribe('workshopSubscriptionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
