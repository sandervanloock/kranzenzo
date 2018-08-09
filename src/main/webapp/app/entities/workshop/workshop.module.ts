import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    WorkshopComponent,
    WorkshopDeleteDialogComponent,
    WorkshopDeletePopupComponent,
    WorkshopDetailComponent,
    WorkshopDialogComponent,
    WorkshopPopupComponent,
    workshopPopupRoute,
    WorkshopPopupService,
    workshopRoute,
    WorkshopService,
} from './';

const ENTITY_STATES = [...workshopRoute, ...workshopPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( ENTITY_STATES )],
               declarations: [WorkshopComponent, WorkshopDetailComponent, WorkshopDialogComponent, WorkshopDeleteDialogComponent, WorkshopPopupComponent,
                              WorkshopDeletePopupComponent,],
               entryComponents: [WorkshopComponent, WorkshopDialogComponent, WorkshopPopupComponent, WorkshopDeleteDialogComponent, WorkshopDeletePopupComponent,],
               providers: [WorkshopService, WorkshopPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoWorkshopModule {
}
