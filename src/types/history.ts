export type HistoryItemType = 'browser' | 'youtube' | 'phone' | 'playStore' | 'calculator' | 'app'

export type HistoryItem = {
    type: HistoryItemType
    label: string
    actionValue: string
}
