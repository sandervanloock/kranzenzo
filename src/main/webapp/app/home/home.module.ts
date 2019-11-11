import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KranzenzoSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [KranzenzoSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class KranzenzoHomeModule {}
