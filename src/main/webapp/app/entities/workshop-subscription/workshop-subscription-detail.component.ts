import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {WorkshopSubscription} from './workshop-subscription.model';
import {WorkshopSubscriptionService} from './workshop-subscription.service';

@Component( {
                selector: 'jhi-workshop-subscription-detail', templateUrl: './workshop-subscription-detail.component.html'
            } )
export class WorkshopSubscriptionDetailComponent implements OnInit, OnDestroy {

    workshopSubscription: WorkshopSubscription;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor( private eventManager: JhiEventManager, private workshopSubscriptionService: WorkshopSubscriptionService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInWorkshopSubscriptions();
    }

    load( id ) {
        this.workshopSubscriptionService.find( id ).subscribe( ( workshopSubscription ) => {
            this.workshopSubscription = workshopSubscription;
        } );
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInWorkshopSubscriptions() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopSubscriptionListModification', ( response ) => this.load( this.workshopSubscription.id ) );
    }
}
