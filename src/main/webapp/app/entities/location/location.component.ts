import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Location} from './location.model';
import {LocationService} from './location.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-location', templateUrl: './location.component.html'
            } )
export class LocationComponent implements OnInit, OnDestroy {
    locations: Location[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private locationService: LocationService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute, private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.locationService.search( {
                                             query: this.currentSearch,
                                         } ).subscribe( ( res: ResponseWrapper ) => this.locations = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.locationService.query().subscribe( ( res: ResponseWrapper ) => {
            this.locations = res.json;
            this.currentSearch = '';
        }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    search( query ) {
        if ( !query ) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then( ( account ) => {
            this.currentAccount = account;
        } );
        this.registerChangeInLocations();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Location ) {
        return item.id;
    }
    registerChangeInLocations() {
        this.eventSubscriber = this.eventManager.subscribe( 'locationListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}
