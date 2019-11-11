import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared/shared.module';
import { WorkshopSubscriptionComponent } from './workshop-subscription.component';
import { WorkshopSubscriptionDetailComponent } from './workshop-subscription-detail.component';
import { WorkshopSubscriptionUpdateComponent } from './workshop-subscription-update.component';
import {
  WorkshopSubscriptionDeletePopupComponent,
  WorkshopSubscriptionDeleteDialogComponent
} from './workshop-subscription-delete-dialog.component';
import { workshopSubscriptionRoute, workshopSubscriptionPopupRoute } from './workshop-subscription.route';

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
  entryComponents: [WorkshopSubscriptionDeleteDialogComponent]
})
export class KranzenzoWorkshopSubscriptionModule {}
