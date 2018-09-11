/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDetailComponent } from 'app/entities/workshop/workshop-detail.component';
import { Workshop } from 'app/shared/model/workshop.model';

describe('Component Tests', () => {
    describe('Workshop Management Detail Component', () => {
        let comp: WorkshopDetailComponent;
        let fixture: ComponentFixture<WorkshopDetailComponent>;
        const route = ({ data: of({ workshop: new Workshop(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WorkshopDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WorkshopDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.workshop).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
