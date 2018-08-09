/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {KransenzoTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {WorkshopDetailComponent} from '../../../../../../main/webapp/app/entities/workshop/workshop-detail.component';
import {WorkshopService} from '../../../../../../main/webapp/app/entities/workshop/workshop.service';
import {Workshop} from '../../../../../../main/webapp/app/entities/workshop/workshop.model';

describe( 'Component Tests', () => {

    describe( 'Workshop Management Detail Component', () => {
        let comp: WorkshopDetailComponent;
        let fixture: ComponentFixture<WorkshopDetailComponent>;
        let service: WorkshopService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule], declarations: [WorkshopDetailComponent], providers: [JhiDateUtils, JhiDataUtils, DatePipe, {
                    provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
                }, WorkshopService, JhiEventManager]
                                            } ).overrideTemplate( WorkshopDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( WorkshopDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( WorkshopService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new Workshop( 10 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.workshop ).toEqual( jasmine.objectContaining( {id: 10} ) );
            } );
        } );
    } );

} );
