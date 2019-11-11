import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';

@Component({
  selector: 'jhi-tag',
  templateUrl: './tag.component.html'
})
export class TagComponent implements OnInit, OnDestroy {
  tags: ITag[];
  eventSubscriber: Subscription;

  constructor(protected tagService: TagService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => {
      this.tags = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInTags();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITag) {
    return item.id;
  }

  registerChangeInTags() {
    this.eventSubscriber = this.eventManager.subscribe('tagListModification', () => this.loadAll());
  }
}
