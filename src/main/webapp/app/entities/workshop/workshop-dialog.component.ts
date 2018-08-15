import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Workshop} from './workshop.model';
import {WorkshopPopupService} from './workshop-popup.service';
import {WorkshopService} from './workshop.service';
import {Tag, TagService} from '../tag';
import {ResponseWrapper} from '../../shared';
import {Image} from '../image/image.model';
import {S3ImageResizePipe} from '../../shared/image/s3-image-resize.pipe';
import {WorkshopDate} from '../workshop-date';

@Component( {
                selector: 'jhi-workshop-dialog', templateUrl: './workshop-dialog.component.html'
            } )
export class WorkshopDialogComponent implements OnInit {

    workshop: Workshop;
    isSaving: boolean;

    tags: Tag[];
    imageEndpoints: string[] = [];

    constructor( public activeModal: NgbActiveModal,
                 private jhiAlertService: JhiAlertService,
                 private workshopService: WorkshopService,
                 private tagService: TagService, private eventManager: JhiEventManager, private s3ImageResizePipe: S3ImageResizePipe ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tagService.query()
            .subscribe( ( res: ResponseWrapper ) => {
                this.tags = res.json;
            }, ( res: ResponseWrapper ) => this.onError( res.json ) );
        if ( this.workshop ) {
            if ( !this.workshop.images ) {
                this.workshop.images = [];
            }
            this.workshop.images.forEach( ( image: Image ) => {
                this.imageEndpoints.push( this.s3ImageResizePipe.transform( image.endpoint, '50x50' ) );
            } );
            this.workshop.dates.forEach( ( workshopDate ) => {
                workshopDate.date = workshopDate.date.substr( 0, 19 );
            } );
        }
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        this.workshop.dates.forEach( ( workshopDate ) => {
            workshopDate.date = workshopDate.date + '+02:00';
        } );
        if ( this.workshop.id !== undefined ) {
            this.subscribeToSaveResponse( this.workshopService.update( this.workshop ) );
        } else {
            this.subscribeToSaveResponse( this.workshopService.create( this.workshop ) );
        }
    }

    trackTagById( index: number, item: Tag ) {
        return item.id;
    }

    getSelected( selectedVals: Array<any>, option: any ) {
        if ( selectedVals ) {
            for ( let i = 0; i < selectedVals.length; i++ ) {
                if ( option.id === selectedVals[i].id ) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    onImageRemoved( $event: any ) {
        this.workshop.images.forEach( ( image: Image, index: number ) => {
            if ( this.s3ImageResizePipe.transform( image.endpoint, '50x50' ) === $event.src ) {
                this.workshop.images.splice( index, 1 );
            }
        } );
    }

    onImageUploadFinished( $event: any ) {
        const id = JSON.parse( $event.serverResponse._body ).id;
        if ( id ) {
            const image = new Image( id );
            this.workshop.images.push( image );
        }
    }

    addWorkshopDate() {
        this.workshop.dates.push( new WorkshopDate() )
    }

    removeWorkshopDate( index ) {
        this.workshop.dates.splice( index, 1 );
    }

    private subscribeToSaveResponse( result: Observable<Workshop> ) {
        result.subscribe( ( res: Workshop ) => this.onSaveSuccess( res ), ( res: Response ) => this.onSaveError() );
    }

    private onSaveSuccess( result: Workshop ) {
        this.eventManager.broadcast( {name: 'workshopListModification', content: 'OK'} );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError( error: any ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}

@Component( {
                selector: 'jhi-workshop-popup', template: ''
            } )
export class WorkshopPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private workshopPopupService: WorkshopPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            if ( params['id'] ) {
                this.workshopPopupService
                    .open( WorkshopDialogComponent as Component, params['id'] );
            } else {
                this.workshopPopupService
                    .open( WorkshopDialogComponent as Component );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
