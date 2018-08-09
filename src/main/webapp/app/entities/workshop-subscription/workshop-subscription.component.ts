import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {WorkshopSubscription} from './workshop-subscription.model';
import {WorkshopSubscriptionService} from './workshop-subscription.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-workshop-subscription', templateUrl: './workshop-subscription.component.html'
            } )
export class WorkshopSubscriptionComponent implements OnInit, OnDestroy {
    workshopSubscriptions: WorkshopSubscription[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private workshopSubscriptionService: WorkshopSubscriptionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.workshopSubscriptionService.search( {
                                                         query: this.currentSearch,
                                                     } ).subscribe( ( res: ResponseWrapper ) => this.workshopSubscriptions = res.json,
                                                                    ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.workshopSubscriptionService.query().subscribe( ( res: ResponseWrapper ) => {
            this.workshopSubscriptions = res.json;
            this.currentSearch = '';
        }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    search( query ) {
        if ( !query ) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then( ( account ) => {
            this.currentAccount = account;
        } );
        this.registerChangeInWorkshopSubscriptions();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: WorkshopSubscription ) {
        return item.id;
    }

    registerChangeInWorkshopSubscriptions() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopSubscriptionListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}
