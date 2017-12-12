import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Customer} from './customer.model';
import {CustomerService} from './customer.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-customer', templateUrl: './customer.component.html'
            } )
export class CustomerComponent implements OnInit, OnDestroy {
    customers: Customer[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private customerService: CustomerService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.customerService.search( {
                                             query: this.currentSearch,
                                         } ).subscribe( ( res: ResponseWrapper ) => this.customers = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.customerService.query().subscribe( ( res: ResponseWrapper ) => {
            this.customers = res.json;
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
        this.registerChangeInCustomers();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Customer ) {
        return item.id;
    }

    registerChangeInCustomers() {
        this.eventSubscriber = this.eventManager.subscribe( 'customerListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}
