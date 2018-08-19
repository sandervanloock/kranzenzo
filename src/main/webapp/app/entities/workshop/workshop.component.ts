import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Workshop} from './workshop.model';
import {WorkshopService} from './workshop.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-workshop', templateUrl: './workshop.component.html'
            } )
export class WorkshopComponent implements OnInit, OnDestroy {
    workshops: Workshop[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private workshopService: WorkshopService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.workshopService.search( {
                                             query: this.currentSearch,
                                         } ).subscribe( ( res: ResponseWrapper ) => this.workshops = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.workshopService.query().subscribe( ( res: ResponseWrapper ) => {
            this.workshops = res.json;
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
        this.registerChangeInWorkshops();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Workshop ) {
        return item.id;
    }

    registerChangeInWorkshops() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}
