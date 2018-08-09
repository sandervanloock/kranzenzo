import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {WorkshopDate} from './workshop-date.model';
import {WorkshopDateService} from './workshop-date.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-workshop-date', templateUrl: './workshop-date.component.html'
            } )
export class WorkshopDateComponent implements OnInit, OnDestroy {
    workshopDates: WorkshopDate[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private workshopDateService: WorkshopDateService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.workshopDateService.search( {
                                                 query: this.currentSearch,
                                             } ).subscribe( ( res: ResponseWrapper ) => this.workshopDates = res.json, ( res: ResponseWrapper ) => this.onError( res.json ) );
            return;
        }
        this.workshopDateService.query().subscribe( ( res: ResponseWrapper ) => {
            this.workshopDates = res.json;
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
        this.registerChangeInWorkshopDates();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: WorkshopDate ) {
        return item.id;
    }

    registerChangeInWorkshopDates() {
        this.eventSubscriber = this.eventManager.subscribe( 'workshopDateListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}
