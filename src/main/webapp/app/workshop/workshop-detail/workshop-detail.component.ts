import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Workshop, WorkshopService} from '../../entities/workshop';
import {Subscription} from 'rxjs/Subscription';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

@Component( {
                selector: 'jhi-workshop-detail', templateUrl: './workshop-detail.component.html', styles: []
            } )
export class WorkshopDetailComponent implements OnInit, OnDestroy {
    workshop: Workshop;
    submittedAlert: any;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor( private eventManager: JhiEventManager, private alertService: JhiAlertService, private workshopService: WorkshopService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInWorkshop();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInWorkshop() {
        //:this.eventSubscriber = this.eventManager.subscribe( 'productListModification', ( response ) => this.load( this.workshop.id ) );
        //this.eventManager.subscribe( 'productOrderCompleted', ( response ) => this.setSubmittedAlert( response.content ) );
    }

    private load( id: number ) {
        this.workshopService.find( id ).subscribe( ( workshop ) => {
            this.workshop = workshop
        } );
    }
}
