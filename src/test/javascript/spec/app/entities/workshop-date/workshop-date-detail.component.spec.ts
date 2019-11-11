import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDateDetailComponent } from 'app/entities/workshop-date/workshop-date-detail.component';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';

describe('Component Tests', () => {
  describe('WorkshopDate Management Detail Component', () => {
    let comp: WorkshopDateDetailComponent;
    let fixture: ComponentFixture<WorkshopDateDetailComponent>;
    const route = ({ data: of({ workshopDate: new WorkshopDate(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopDateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorkshopDateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkshopDateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workshopDate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
