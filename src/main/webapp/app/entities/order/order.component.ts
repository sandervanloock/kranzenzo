import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Order} from './order.model';
import {OrderService} from './order.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-order', templateUrl: './order.component.html'
            } )
export class OrderComponent implements OnInit, OnDestroy {
    orders: Order[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private orderService: OrderService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.orderService.search( {
                                          query: this.currentSearch,
                                      } ).subscribe( ( res: ResponseWrapper ) => this.orders = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.orderService.query().subscribe( ( res: ResponseWrapper ) => {
            this.orders = res.json;
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
        this.registerChangeInOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Order ) {
        return item.id;
    }

    registerChangeInOrders() {
        this.eventSubscriber = this.eventManager.subscribe( 'orderListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}
