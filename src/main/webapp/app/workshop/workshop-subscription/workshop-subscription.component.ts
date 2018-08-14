import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Workshop, WorkshopPopupService} from '../../entities/workshop';
import {WorkshopDate} from '../../entities/workshop-date';
import {User} from '../../shared';
import {SubscriptionState, WorkshopSubscription, WorkshopSubscriptionService} from '../../entities/workshop-subscription';

@Component( {
                selector: 'jhi-workshop-subscription', templateUrl: './workshop-subscription.component.html', styles: []
            } )
export class WorkshopSubscriptionComponent implements OnInit {
    user: User = new User();
    workshop: Workshop = new Workshop();
    workshopDate: WorkshopDate = new WorkshopDate();

    constructor( private route: ActivatedRoute, public workshopPopupService: WorkshopPopupService, private workshopSubscriptionService: WorkshopSubscriptionService, ) {
    }

    ngOnInit() {
        this.route.data.subscribe( ( data ) => {
            this.load( data['date'] ); //TODO correct date
        } );
    }

    submitForm() {
        const subscription = new WorkshopSubscription();
        subscription.state = SubscriptionState.NEW;
        subscription.workshopId = this.workshopDate.id;
        subscription.user = this.user;

        this.workshopSubscriptionService.create( subscription ).subscribe();

        this.workshopPopupService.close();
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
