import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Workshop} from './workshop.model';
import {WorkshopService} from './workshop.service';

@Injectable()
export class WorkshopPopupService {
    private ngbModalRef: NgbModalRef;

    constructor( private modalService: NgbModal, private router: Router, private workshopService: WorkshopService ) {
        this.ngbModalRef = null;
    }

    open( component: Component, id?: number | any ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>( ( resolve, reject ) => {
            const isOpen = this.ngbModalRef !== null;
            if ( isOpen ) {
                resolve( this.ngbModalRef );
            }

            if ( id ) {
                this.workshopService.find( id ).subscribe( ( workshop ) => {
                    this.ngbModalRef = this.workshopModalRef( component, workshop );
                    resolve( this.ngbModalRef );
                } );
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout( () => {
                    this.ngbModalRef = this.workshopModalRef( component, new Workshop() );
                    resolve( this.ngbModalRef );
                }, 0 );
            }
        } );
    }

    close() {
        this.ngbModalRef.close();
    }

    workshopModalRef( component: Component, workshop: Workshop ): NgbModalRef {
        const modalRef = this.modalService.open( component, {size: 'lg', backdrop: 'static'} );
        modalRef.componentInstance.workshop = workshop;
        modalRef.result.then( ( result ) => {
            this.router.navigate( [{outlets: {popup: null}}], {replaceUrl: true} );
            this.ngbModalRef = null;
        }, ( reason ) => {
            this.router.navigate( [{outlets: {popup: null}}], {replaceUrl: true} );
            this.ngbModalRef = null;
        } );
        return modalRef;
    }
}
