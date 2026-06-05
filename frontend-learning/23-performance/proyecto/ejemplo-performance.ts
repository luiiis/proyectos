// Angular Performance - Reference File
import { Component, ChangeDetectionStrategy } from '@angular/core';
import { ScrollingModule } from '@angular/cdk/scrolling';

interface Item { id: number; name: string; }

// --- OnPush Change Detection ---
@Component({
  selector: 'app-perf-demo',
  standalone: true,
  imports: [ScrollingModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- trackBy for efficient list rendering -->
    @for (item of items; track item.id) {
      <div>{{ item.name }}</div>
    }

    <!-- @defer for lazy loading sections -->
    @defer (on viewport) {
      <app-heavy-chart />
    } @placeholder {
      <p>Loading chart...</p>
    } @loading (minimum 500ms) {
      <p>Fetching data...</p>
    }

    <!-- Virtual Scrolling for large lists -->
    <cdk-virtual-scroll-viewport itemSize="48" class="viewport">
      <div *cdkVirtualFor="let item of largeList; trackBy: trackById" class="item">
        {{ item.name }}
      </div>
    </cdk-virtual-scroll-viewport>
  `,
  styles: [`.viewport { height: 400px; width: 100%; }`]
})
export class PerfDemoComponent {
  items: Item[] = Array.from({ length: 100 }, (_, i) => ({ id: i, name: `Item ${i}` }));
  largeList: Item[] = Array.from({ length: 10000 }, (_, i) => ({ id: i, name: `Row ${i}` }));

  trackById(index: number, item: Item): number {
    return item.id;
  }
}

console.log('Reference file: ejemplo-performance.ts - OnPush, trackBy, @defer & virtual scroll for use in an Angular project');
