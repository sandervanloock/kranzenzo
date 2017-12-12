import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Customer} from './customer.model';
import {CustomerPopupService} from './customer-popup.service';
import {CustomerService} from './customer.service';
import {Location, LocationService} from '../location';
import {ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-customer-dialog', templateUrl: './customer-dialog.component.html'
            } )
export class CustomerDialogComponent implements OnInit {

    customer: Customer;
    isSaving: boolean;

    addresses: Location[];

    constructor( public activeModal: NgbActiveModal,
                 private alertService: JhiAlertService,
                 private customerService: CustomerService,
                 private locationService: LocationService,
                 private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.locationService
            .query( {filter: 'customer-is-null'} )
            .subscribe( ( res: ResponseWrapper ) => {
                if ( !this.customer.addressId ) {
                    this.addresses = res.json;
                } else {
                    this.locationService
                        .find( this.customer.addressId )
                        .subscribe( ( subRes: Location ) => {
                            this.addresses = [subRes].concat( res.json );
                        }, ( subRes: ResponseWrapper ) => this.onError( subRes.json ) );
                }
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.customer.id !== undefined ) {
            this.subscribeToSaveResponse( this.customerService.update( this.customer ) );
        } else {
            this.subscribeToSaveResponse( this.customerService.create( this.customer ) );
        }
    }

    trackLocationById( index: number, item: Location ) {
        return item.id;
    }

    private subscribeToSaveResponse( result: Observable<Customer> ) {
        result.subscribe( ( res: Customer ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError( res ) );
    }

    private onSaveSuccess( result: Customer ) {
        this.eventManager.broadcast( {name: 'customerListModification', content: 'OK'} );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    private onSaveError( error ) {
        try {
            error.json();
        } catch ( exception ) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError( error );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}

@Component( {
                selector: 'jhi-customer-popup', template: ''
            } )
export class CustomerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private customerPopupService: CustomerPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.customerPopupService
                    .open( CustomerDialogComponent as Component, params['id'] );
            } else {
                this.customerPopupService
                    .open( CustomerDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
