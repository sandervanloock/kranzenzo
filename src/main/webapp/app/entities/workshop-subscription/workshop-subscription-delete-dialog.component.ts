import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {WorkshopSubscription} from './workshop-subscription.model';
import {WorkshopSubscriptionPopupService} from './workshop-subscription-popup.service';
import {WorkshopSubscriptionService} from './workshop-subscription.service';

@Component( {
                selector: 'jhi-workshop-subscription-delete-dialog', templateUrl: './workshop-subscription-delete-dialog.component.html'
            } )
export class WorkshopSubscriptionDeleteDialogComponent {

    workshopSubscription: WorkshopSubscription;

    constructor( private workshopSubscriptionService: WorkshopSubscriptionService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager ) {
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    confirmDelete( id: number ) {
        this.workshopSubscriptionService.delete( id ).subscribe( ( response ) => {
            this.eventManager.broadcast( {
                                             name: 'workshopSubscriptionListModification', content: 'Deleted an workshopSubscription'
                                         } );
            this.activeModal.dismiss( true );
        } );
    }
}

@Component( {
                selector: 'jhi-workshop-subscription-delete-popup', template: ''
            } )
export class WorkshopSubscriptionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopSubscriptionPopupService: WorkshopSubscriptionPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            this.workshopSubscriptionPopupService
                .open( WorkshopSubscriptionDeleteDialogComponent as Component, params['id'] );
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
