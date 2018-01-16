import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Order} from './order.model';
import {OrderPopupService} from './order-popup.service';
import {OrderService} from './order.service';
import {Customer, CustomerService} from '../customer';
import {Location, LocationService} from '../location';
import {Product, ProductService} from '../product';
import {ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-order-dialog', templateUrl: './order-dialog.component.html'
            } )
export class OrderDialogComponent implements OnInit {

    order: Order;
    isSaving: boolean;

    customers: Customer[];

    deliveryaddresses: Location[];

    products: Product[];

    constructor( public activeModal: NgbActiveModal,
                 private jhiAlertService: JhiAlertService,
                 private orderService: OrderService,
                 private customerService: CustomerService,
                 private locationService: LocationService,
                 private productService: ProductService,
                 private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customerService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.customers = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
        this.locationService
            .query( {filter: 'order-is-null'} )
            .subscribe( ( res: ResponseWrapper ) => {
                if ( !this.order.deliveryAddressId ) {
                    this.deliveryaddresses = res.json;
                } else {
                    this.locationService
                        .find( this.order.deliveryAddressId )
                        .subscribe( ( subRes: Location ) => {
                            this.deliveryaddresses = [subRes].concat( res.json );
                        }, ( subRes: ResponseWrapper ) => this.onError( subRes.json ) );
                }
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
        this.productService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.products = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.order.id !== undefined ) {
            this.subscribeToSaveResponse( this.orderService.update( this.order ) );
        } else {
            this.subscribeToSaveResponse( this.orderService.create( this.order ) );
        }
    }

    trackCustomerById( index: number, item: Customer ) {
        return item.id;
    }

    trackLocationById( index: number, item: Location ) {
        return item.id;
    }

    trackProductById( index: number, item: Product ) {
        return item.id;
    }

    private subscribeToSaveResponse( result: Observable<Order> ) {
        result.subscribe( ( res: Order ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    private onSaveSuccess( result: Order ) {
        this.eventManager.broadcast( {name: 'orderListModification', content: 'OK'} );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError( error: any ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}

@Component( {
                selector: 'jhi-order-popup', template: ''
            } )
export class OrderPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private orderPopupService: OrderPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.orderPopupService
                    .open( OrderDialogComponent as Component, params['id'] );
            } else {
                this.orderPopupService
                    .open( OrderDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
