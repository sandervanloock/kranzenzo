import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared';
import {
    WorkshopSubscriptionComponent,
    WorkshopSubscriptionDeleteDialogComponent,
    WorkshopSubscriptionDeletePopupComponent,
    WorkshopSubscriptionDetailComponent,
    workshopSubscriptionPopupRoute,
    workshopSubscriptionRoute,
    WorkshopSubscriptionUpdateComponent
} from './';

const ENTITY_STATES = [...workshopSubscriptionRoute, ...workshopSubscriptionPopupRoute];

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WorkshopSubscriptionComponent,
        WorkshopSubscriptionDetailComponent,
        WorkshopSubscriptionUpdateComponent,
        WorkshopSubscriptionDeleteDialogComponent,
        WorkshopSubscriptionDeletePopupComponent
    ],
    entryComponents: [
        WorkshopSubscriptionComponent,
        WorkshopSubscriptionUpdateComponent,
        WorkshopSubscriptionDeleteDialogComponent,
        WorkshopSubscriptionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoWorkshopSubscriptionModule {}
