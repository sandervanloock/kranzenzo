import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Workshop, WorkshopPopupService} from '../../entities/workshop';
import {WorkshopDate} from '../../entities/workshop-date';
import {User} from '../../shared';

@Component( {
                selector: 'jhi-workshop-subscription', templateUrl: './workshop-subscription.component.html', styles: []
            } )
export class WorkshopSubscriptionComponent implements OnInit {
    user: User = new User();
    workshop: Workshop = new Workshop();
    workshopDate: WorkshopDate = new WorkshopDate();

    constructor( private route: ActivatedRoute, public workshopPopupService: WorkshopPopupService, ) {
    }

    ngOnInit() {
        this.route.data.subscribe( ( data ) => {
            this.load( data['date'] );
        } );
    }

    submitForm() {
        /*this.updateCustomer().flatMap( ( customer ) => {
            this.customer = customer;
            this.order.customerId = customer.id;
            return this.orderService.create( this.order );
        } ).subscribe( ( order: Order ) => {
            this.handleSuccessfulOrder( order );
        }, ( error ) => {
            this.handleFailedOrder();
        } )*/
    }

    private load( id: number ) {
        this.workshopDate = this.workshop.dates[0]; //TODO
    }
}

@Component( {
                selector: 'jhi-workshop-subscription-popup', template: ''
            } )
export class WorkshopSubscriptionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopPopupService: WorkshopPopupService ) {
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
