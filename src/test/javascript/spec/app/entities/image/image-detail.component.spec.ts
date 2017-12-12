/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {OnInit} from '@angular/core';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {KransenzoTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {ImageDetailComponent} from '../../../../../../main/webapp/app/entities/image/image-detail.component';
import {ImageService} from '../../../../../../main/webapp/app/entities/image/image.service';
import {Image} from '../../../../../../main/webapp/app/entities/image/image.model';

describe( 'Component Tests', () => {

    describe( 'Image Management Detail Component', () => {
        let comp: ImageDetailComponent;
        let fixture: ComponentFixture<ImageDetailComponent>;
        let service: ImageService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule], declarations: [ImageDetailComponent], providers: [JhiDateUtils, JhiDataUtils, DatePipe, {
                    provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
                }, ImageService, JhiEventManager]
                                            } ).overrideTemplate( ImageDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( ImageDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( ImageService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new Image( 10 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.image ).toEqual( jasmine.objectContaining( {id: 10} ) );
            } );
        } );
    } );

} );
