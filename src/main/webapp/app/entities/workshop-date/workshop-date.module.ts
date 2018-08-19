import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    WorkshopDateComponent,
    WorkshopDateDeleteDialogComponent,
    WorkshopDateDeletePopupComponent,
    WorkshopDateDetailComponent,
    WorkshopDateDialogComponent,
    WorkshopDatePopupComponent,
    workshopDatePopupRoute,
    WorkshopDatePopupService,
    workshopDateRoute,
    WorkshopDateService,
} from './';

const ENTITY_STATES = [...workshopDateRoute, ...workshopDatePopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( ENTITY_STATES )],
               declarations: [WorkshopDateComponent, WorkshopDateDetailComponent, WorkshopDateDialogComponent, WorkshopDateDeleteDialogComponent, WorkshopDatePopupComponent,
                              WorkshopDateDeletePopupComponent,],
               entryComponents: [WorkshopDateComponent, WorkshopDateDialogComponent, WorkshopDatePopupComponent, WorkshopDateDeleteDialogComponent,
                                 WorkshopDateDeletePopupComponent,],
               providers: [WorkshopDateService, WorkshopDatePopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoWorkshopDateModule {
}
