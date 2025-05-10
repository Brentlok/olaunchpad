export const capitalize = <TText extends string>(text: TText) => (text.charAt(0).toUpperCase() + text.slice(1)) as Capitalize<TText>
