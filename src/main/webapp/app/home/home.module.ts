import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ShareModule} from '@ngx-share/core';

import {KransenzoSharedModule} from '../shared';

import {HOME_ROUTE, HomeComponent} from './';

@NgModule({
              imports: [KransenzoSharedModule, RouterModule.forChild( HOME_ROUTE ), ShareModule.forRoot()], declarations: [HomeComponent], entryComponents: [],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KransenzoHomeModule {}
