import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {DatePipe} from '@angular/common';
import {WorkshopDate} from './workshop-date.model';
import {WorkshopDateService} from './workshop-date.service';

@Injectable()
export class WorkshopDatePopupService {
    private ngbModalRef: NgbModalRef;

    constructor( private datePipe: DatePipe, private modalService: NgbModal, private router: Router, private workshopDateService: WorkshopDateService ) {
        this.ngbModalRef = null;
    }

    open( component: Component, id?: number | any ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>( ( resolve, reject ) => {
            const isOpen = this.ngbModalRef !== null;
            if ( isOpen ) {
                resolve( this.ngbModalRef );
            }

            if ( id ) {
                this.workshopDateService.find( id ).subscribe( ( workshopDate ) => {
                    workshopDate.date = this.datePipe
                        .transform( workshopDate.date, 'yyyy-MM-ddTHH:mm:ss' );
                    this.ngbModalRef = this.workshopDateModalRef( component, workshopDate );
                    resolve( this.ngbModalRef );
                } );
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout( () => {
                    this.ngbModalRef = this.workshopDateModalRef( component, new WorkshopDate() );
                    resolve( this.ngbModalRef );
                }, 0 );
            }
        } );
    }

    workshopDateModalRef( component: Component, workshopDate: WorkshopDate ): NgbModalRef {
        const modalRef = this.modalService.open( component, {size: 'lg', backdrop: 'static'} );
        modalRef.componentInstance.workshopDate = workshopDate;
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
