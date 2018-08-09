import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {WorkshopSubscription} from './workshop-subscription.model';
import {WorkshopSubscriptionPopupService} from './workshop-subscription-popup.service';
import {WorkshopSubscriptionService} from './workshop-subscription.service';
import {WorkshopDate, WorkshopDateService} from '../workshop-date';
import {ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-workshop-subscription-dialog', templateUrl: './workshop-subscription-dialog.component.html'
            } )
export class WorkshopSubscriptionDialogComponent implements OnInit {

    workshopSubscription: WorkshopSubscription;
    isSaving: boolean;

    workshopdates: WorkshopDate[];

    constructor( public activeModal: NgbActiveModal,
                 private jhiAlertService: JhiAlertService,
                 private workshopSubscriptionService: WorkshopSubscriptionService,
                 private workshopDateService: WorkshopDateService,
                 private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.workshopDateService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.workshopdates = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.workshopSubscription.id !== undefined ) {
            this.subscribeToSaveResponse( this.workshopSubscriptionService.update( this.workshopSubscription ) );
        } else {
            this.subscribeToSaveResponse( this.workshopSubscriptionService.create( this.workshopSubscription ) );
        }
    }

    trackWorkshopDateById( index: number, item: WorkshopDate ) {
        return item.id;
    }

    private subscribeToSaveResponse( result: Observable<WorkshopSubscription> ) {
        result.subscribe( ( res: WorkshopSubscription ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    private onSaveSuccess( result: WorkshopSubscription ) {
        this.eventManager.broadcast( {name: 'workshopSubscriptionListModification', content: 'OK'} );
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
                selector: 'jhi-workshop-subscription-popup', template: ''
            } )
export class WorkshopSubscriptionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopSubscriptionPopupService: WorkshopSubscriptionPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.workshopSubscriptionPopupService
                    .open( WorkshopSubscriptionDialogComponent as Component, params['id'] );
            } else {
                this.workshopSubscriptionPopupService
                    .open( WorkshopSubscriptionDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
