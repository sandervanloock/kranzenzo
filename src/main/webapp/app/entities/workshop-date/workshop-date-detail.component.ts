import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription';
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'jhi-workshop-date-detail',
    templateUrl: './workshop-date-detail.component.html'
})
export class WorkshopDateDetailComponent implements OnInit {
    workshopDate: IWorkshopDate;
    subscriptions: IWorkshopSubscription[];

    private subscription: Subscription;

    constructor(
        private activatedRoute: ActivatedRoute,
        private subscriptionService: WorkshopSubscriptionService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    previousState() {
        window.history.back();
    }

    private load(id: any) {
        this.activatedRoute.data.subscribe(({ workshopDate }) => {
            this.workshopDate = workshopDate;
        });
        this.subscriptionService.findByWorkshopDate(id).subscribe(subscriptions => {
            this.subscriptions = subscriptions.body;
        });
    }
}
