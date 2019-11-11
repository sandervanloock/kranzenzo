import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared/shared.module';
import { ImageComponent } from './image.component';
import { ImageDetailComponent } from './image-detail.component';
import { ImageUpdateComponent } from './image-update.component';
import { ImageDeletePopupComponent, ImageDeleteDialogComponent } from './image-delete-dialog.component';
import { imageRoute, imagePopupRoute } from './image.route';

const ENTITY_STATES = [...imageRoute, ...imagePopupRoute];

@NgModule({
  imports: [KranzenzoSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ImageComponent, ImageDetailComponent, ImageUpdateComponent, ImageDeleteDialogComponent, ImageDeletePopupComponent],
  entryComponents: [ImageDeleteDialogComponent]
})
export class KranzenzoImageModule {}
