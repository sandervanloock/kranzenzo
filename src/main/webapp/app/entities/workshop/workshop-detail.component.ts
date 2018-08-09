import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager} from 'ng-jhipster';

import {Workshop} from './workshop.model';
import {WorkshopService} from './workshop.service';

@Component( {
                selector: 'jhi-workshop-detail', templateUrl: './workshop-detail.component.html'
            } )
export class WorkshopDetailComponent implements OnInit, OnDestroy {

    workshop: Workshop;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor( private eventManager: JhiEventManager, private workshopService: WorkshopService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInWorkshops();
    }

    load( id ) {
        this.workshopService.find( id ).subscribe( ( workshop ) => {
            this.workshop = workshop;
        } );
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInWorkshops() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopListModification', ( response ) => this.load( this.workshop.id ) );
    }
}
