/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {KransenzoTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {WorkshopDateDetailComponent} from '../../../../../../main/webapp/app/entities/workshop-date/workshop-date-detail.component';
import {WorkshopDateService} from '../../../../../../main/webapp/app/entities/workshop-date/workshop-date.service';
import {WorkshopDate} from '../../../../../../main/webapp/app/entities/workshop-date/workshop-date.model';

describe( 'Component Tests', () => {

    describe( 'WorkshopDate Management Detail Component', () => {
        let comp: WorkshopDateDetailComponent;
        let fixture: ComponentFixture<WorkshopDateDetailComponent>;
        let service: WorkshopDateService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule], declarations: [WorkshopDateDetailComponent], providers: [JhiDateUtils, JhiDataUtils, DatePipe, {
                    provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
                }, WorkshopDateService, JhiEventManager]
                                            } ).overrideTemplate( WorkshopDateDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( WorkshopDateDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( WorkshopDateService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new WorkshopDate( 10 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.workshopDate ).toEqual( jasmine.objectContaining( {id: 10} ) );
            } );
        } );
    } );

} );
