import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Workshop, WorkshopPopupService} from '../../entities/workshop';
import {WorkshopDate, WorkshopDateService} from '../../entities/workshop-date';

@Component( {
                selector: 'jhi-workshop-subscription', templateUrl: './workshop-subscription.component.html', styles: []
            } )
export class WorkshopSubscriptionComponent implements OnInit {
    workshop: Workshop = new Workshop();
    workshopDate: WorkshopDate;

    constructor( private route: ActivatedRoute, private workshopDateService: WorkshopDateService ) {
    }

    ngOnInit() {
        this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
    }

    private load( id: number ) {
        this.workshopDateService.find( id ).subscribe( ( workshopDate ) => {
            this.workshopDate = workshopDate
        } );
    }
}

@Component( {
                selector: 'jhi-workshop-subscription-popup', template: ''
            } )
export class WorkshopSubscriptionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopPopupService: WorkshopPopupService, private workshopDateService: WorkshopDateService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            this.workshopPopupService
                .open( WorkshopSubscriptionComponent as Component, params['id'] );
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

