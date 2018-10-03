import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductOrderComponent } from 'app/shop/order/product-order.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUser, User } from 'app/core';
import { Workshop } from 'app/shared/model/workshop.model';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription';
import { SubscriptionState, WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

@Component({
    selector: 'jhi-workshop-subscription',
    templateUrl: './workshop-subscription.component.html',
    styles: []
})
export class WorkshopSubscriptionComponent implements OnInit {
    user: IUser = new User();
    workshop: Workshop = new Workshop();
    workshopDate: WorkshopDate = new WorkshopDate();

    constructor(
        private route: ActivatedRoute,
        private workshopSubscriptionService: WorkshopSubscriptionService,
        public activeModal: NgbActiveModal
    ) {}

    ngOnInit() {
        this.route.children[2].params.subscribe(params => {
            this.load(parseInt(params['date'], 10));
        });
    }

    submitForm() {
        const subscription = new WorkshopSubscription();
        subscription.state = SubscriptionState.NEW;
        subscription.workshopId = this.workshopDate.id;
        subscription.user = this.user;

        this.workshopSubscriptionService.create(subscription).subscribe();

        this.activeModal.close();
    }

    load(id: number) {
        this.workshopDate = this.workshop.dates.find(date => {
            return date.id === id;
        });
    }
}

@Component({
    selector: 'jhi-workshop-subscription-popup',
    template: ''
})
export class WorkshopSubscriptionPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ workshop }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WorkshopSubscriptionComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.workshop = workshop;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
