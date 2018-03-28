import './vendor.ts';

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {Ng2Webstorage} from 'ng2-webstorage';

import {KransenzoSharedModule, UserRouteAccessService} from './shared';
import {KransenzoAppRoutingModule} from './app-routing.module';
import {KransenzoHomeModule} from './home/home.module';
import {KransenzoAdminModule} from './admin/admin.module';
import {KransenzoAccountModule} from './account/account.module';
import {KransenzoEntityModule} from './entities/entity.module';
import {KransenzoInfoModule} from './info/info.module';
import {customHttpProvider} from './blocks/interceptor/http.provider';
import {PaginationConfig} from './blocks/config/uib-pagination.config';
import {ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent, ProfileService} from './layouts';
import {KransenzoShopModule} from './shop/shop.module';

// jhipster-needle-angular-add-module-import JHipster will add new module here

@NgModule({
    imports: [
        BrowserModule,
        KransenzoAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        KransenzoSharedModule,
        KransenzoHomeModule,
        KransenzoAdminModule,
        KransenzoAccountModule, KransenzoEntityModule, KransenzoInfoModule, KransenzoShopModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class KransenzoAppModule {}
