import { Component, OnDestroy, OnInit } from '@angular/core';
import { IWorkshop, Workshop } from 'app/shared/model/workshop.model';
import { Subscription } from 'rxjs/Subscription';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { WorkshopService } from 'app/entities/workshop';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-workshop-detail',
    templateUrl: './workshop-detail.component.html',
    styles: []
})
export class WorkshopDetailComponent implements OnInit, OnDestroy {
    workshop: IWorkshop;
    submittedAlert: any;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private alertService: JhiAlertService,
        private workshopService: WorkshopService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    private load(id: number) {
        this.workshopService.find(id, true).subscribe(workshop => {
            this.workshop = workshop.body;
        });
    }
}
