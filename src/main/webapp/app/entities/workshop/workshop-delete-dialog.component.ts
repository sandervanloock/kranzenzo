import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Workshop} from './workshop.model';
import {WorkshopPopupService} from './workshop-popup.service';
import {WorkshopService} from './workshop.service';

@Component( {
                selector: 'jhi-workshop-delete-dialog', templateUrl: './workshop-delete-dialog.component.html'
            } )
export class WorkshopDeleteDialogComponent {

    workshop: Workshop;

    constructor( private workshopService: WorkshopService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager ) {
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    confirmDelete( id: number ) {
        this.workshopService.delete( id ).subscribe( ( response ) => {
            this.eventManager.broadcast( {
                                             name: 'workshopListModification', content: 'Deleted an workshop'
                                         } );
            this.activeModal.dismiss( true );
        } );
    }
}

@Component( {
                selector: 'jhi-workshop-delete-popup', template: ''
            } )
export class WorkshopDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopPopupService: WorkshopPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            this.workshopPopupService
                .open( WorkshopDeleteDialogComponent as Component, params['id'] );
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
