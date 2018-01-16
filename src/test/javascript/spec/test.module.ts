import {DatePipe} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {ElementRef, NgModule, Renderer} from '@angular/core';
import {MockBackend} from '@angular/http/testing';
import {BaseRequestOptions, Http} from '@angular/http';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiLanguageService, JhiParseLinks} from 'ng-jhipster';

import {MockLanguageHelper, MockLanguageService} from './helpers/mock-language.service';
import {AccountService, JhiLanguageHelper, LoginModalService, Principal} from '../../../main/webapp/app/shared';
import {MockPrincipal} from './helpers/mock-principal.service';
import {MockAccountService} from './helpers/mock-account.service';
import {MockActivatedRoute, MockRouter} from './helpers/mock-route.service';
import {MockActiveModal} from './helpers/mock-active-modal.service';
import {MockEventManager} from './helpers/mock-event-manager.service';

@NgModule( {
               providers: [DatePipe, JhiDataUtils, JhiDateUtils, JhiParseLinks, MockBackend, BaseRequestOptions, {
                   provide: JhiLanguageService, useClass: MockLanguageService
               }, {
                   provide: JhiLanguageHelper, useClass: MockLanguageHelper
               }, {
                   provide: JhiEventManager, useClass: MockEventManager
               }, {
                   provide: NgbActiveModal, useClass: MockActiveModal
               }, {
                   provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
               }, {
                   provide: Router, useClass: MockRouter
               }, {
                   provide: Principal, useClass: MockPrincipal
               }, {
                   provide: AccountService, useClass: MockAccountService
               }, {
                   provide: LoginModalService, useValue: null
               }, {
                   provide: ElementRef, useValue: null
               }, {
                   provide: Renderer, useValue: null
               }, {
                   provide: JhiAlertService, useValue: null
               }, {
                   provide: NgbModal, useValue: null
               }, {
                   provide: Http, useFactory: ( backendInstance: MockBackend, defaultOptions: BaseRequestOptions ) => {
                       return new Http( backendInstance, defaultOptions );
                   }, deps: [MockBackend, BaseRequestOptions]
               }]
           } )
export class KransenzoTestModule {
}
