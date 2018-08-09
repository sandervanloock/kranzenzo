import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    WorkshopSubscriptionComponent,
    WorkshopSubscriptionDeleteDialogComponent,
    WorkshopSubscriptionDeletePopupComponent,
    WorkshopSubscriptionDetailComponent,
    WorkshopSubscriptionDialogComponent,
    WorkshopSubscriptionPopupComponent,
    workshopSubscriptionPopupRoute,
    WorkshopSubscriptionPopupService,
    workshopSubscriptionRoute,
    WorkshopSubscriptionService,
} from './';

const ENTITY_STATES = [...workshopSubscriptionRoute, ...workshopSubscriptionPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( ENTITY_STATES )],
               declarations: [WorkshopSubscriptionComponent, WorkshopSubscriptionDetailComponent, WorkshopSubscriptionDialogComponent, WorkshopSubscriptionDeleteDialogComponent,
                              WorkshopSubscriptionPopupComponent, WorkshopSubscriptionDeletePopupComponent,],
               entryComponents: [WorkshopSubscriptionComponent, WorkshopSubscriptionDialogComponent, WorkshopSubscriptionPopupComponent, WorkshopSubscriptionDeleteDialogComponent,
                                 WorkshopSubscriptionDeletePopupComponent,],
               providers: [WorkshopSubscriptionService, WorkshopSubscriptionPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoWorkshopSubscriptionModule {
}
