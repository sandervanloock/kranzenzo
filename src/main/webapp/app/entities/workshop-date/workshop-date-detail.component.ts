import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {WorkshopDate} from './workshop-date.model';
import {WorkshopDateService} from './workshop-date.service';
import {WorkshopSubscription, WorkshopSubscriptionService} from '../workshop-subscription';

@Component( {
                selector: 'jhi-workshop-date-detail', templateUrl: './workshop-date-detail.component.html'
            } )
export class WorkshopDateDetailComponent implements OnInit, OnDestroy {

    workshopDate: WorkshopDate;
    subscriptions: WorkshopSubscription[];
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private workshopDateService: WorkshopDateService,
        private subscriptionService: WorkshopSubscriptionService,
        private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInWorkshopDates();
    }

    load( id ) {
        this.workshopDateService.find( id ).subscribe( ( workshopDate ) => {
            this.workshopDate = workshopDate;
        } );
        this.subscriptionService.findByWorkshopDate( id ).subscribe( ( subscriptions ) => {
            this.subscriptions = subscriptions.json;
        } )
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInWorkshopDates() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopDateListModification', ( response ) => this.load( this.workshopDate.id ) );
    }
}
