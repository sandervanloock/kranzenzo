/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {Observable} from 'rxjs/Observable';

import {KransenzoTestModule} from '../../../test.module';
import {TagDetailComponent} from '../../../../../../main/webapp/app/entities/tag/tag-detail.component';
import {TagService} from '../../../../../../main/webapp/app/entities/tag/tag.service';
import {Tag} from '../../../../../../main/webapp/app/entities/tag/tag.model';

describe( 'Component Tests', () => {

    describe( 'Tag Management Detail Component', () => {
        let comp: TagDetailComponent;
        let fixture: ComponentFixture<TagDetailComponent>;
        let service: TagService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule], declarations: [TagDetailComponent], providers: [TagService]
                                            } )
                .overrideTemplate( TagDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( TagDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( TagService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new Tag( 123 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.tag ).toEqual( jasmine.objectContaining( {id: 123} ) );
            } );
        } );
    } );

} );
