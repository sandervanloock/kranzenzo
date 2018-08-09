import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {WorkshopDate} from './workshop-date.model';
import {WorkshopDatePopupService} from './workshop-date-popup.service';
import {WorkshopDateService} from './workshop-date.service';

@Component( {
                selector: 'jhi-workshop-date-delete-dialog', templateUrl: './workshop-date-delete-dialog.component.html'
            } )
export class WorkshopDateDeleteDialogComponent {

    workshopDate: WorkshopDate;

    constructor( private workshopDateService: WorkshopDateService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager ) {
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    confirmDelete( id: number ) {
        this.workshopDateService.delete( id ).subscribe( ( response ) => {
            this.eventManager.broadcast( {
                                             name: 'workshopDateListModification', content: 'Deleted an workshopDate'
                                         } );
            this.activeModal.dismiss( true );
        } );
    }
}

@Component( {
                selector: 'jhi-workshop-date-delete-popup', template: ''
            } )
export class WorkshopDateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopDatePopupService: WorkshopDatePopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            this.workshopDatePopupService
                .open( WorkshopDateDeleteDialogComponent as Component, params['id'] );
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
