import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {WorkshopDate} from './workshop-date.model';
import {WorkshopDatePopupService} from './workshop-date-popup.service';
import {WorkshopDateService} from './workshop-date.service';
import {Workshop, WorkshopService} from '../workshop';
import {ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-workshop-date-dialog', templateUrl: './workshop-date-dialog.component.html'
            } )
export class WorkshopDateDialogComponent implements OnInit {

    workshopDate: WorkshopDate;
    isSaving: boolean;

    workshops: Workshop[];

    constructor( public activeModal: NgbActiveModal,
                 private jhiAlertService: JhiAlertService,
                 private workshopDateService: WorkshopDateService,
                 private workshopService: WorkshopService,
                 private eventManager: JhiEventManager ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.workshopService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.workshops = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.workshopDate.id !== undefined ) {
            this.subscribeToSaveResponse( this.workshopDateService.update( this.workshopDate ) );
        } else {
            this.subscribeToSaveResponse( this.workshopDateService.create( this.workshopDate ) );
        }
    }

    trackWorkshopById( index: number, item: Workshop ) {
        return item.id;
    }

    private subscribeToSaveResponse( result: Observable<WorkshopDate> ) {
        result.subscribe( ( res: WorkshopDate ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    private onSaveSuccess( result: WorkshopDate ) {
        this.eventManager.broadcast( {name: 'workshopDateListModification', content: 'OK'} );
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
                selector: 'jhi-workshop-date-popup', template: ''
            } )
export class WorkshopDatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopDatePopupService: WorkshopDatePopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.workshopDatePopupService
                    .open( WorkshopDateDialogComponent as Component, params['id'] );
            } else {
                this.workshopDatePopupService
                    .open( WorkshopDateDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
